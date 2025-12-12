$version = '4.10.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'D8D81F11EAF2D7E0EC2E149E47266720669F3F63DD4D170B0267BAE6585B2B12'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
