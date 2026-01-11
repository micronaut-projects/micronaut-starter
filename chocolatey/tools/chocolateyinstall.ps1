$version = '4.10.7'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '60F3BAA7E0D57941E6D33334062104A29B492F68577276EABBD341FA18FE9B6E'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
