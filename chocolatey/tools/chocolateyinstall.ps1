$version  = '2.0.0.RC1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'a4c195d4ff04e03a9ef689eeb3a6bce0dc64c9c327da8251dfb9b46958dda2b1'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs