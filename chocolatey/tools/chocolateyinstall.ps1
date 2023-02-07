$version = '3.5.7'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '953A6898708071BCE0C3D7732ABF10D41FE137B88A8EE6F50403A602404C7812'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
