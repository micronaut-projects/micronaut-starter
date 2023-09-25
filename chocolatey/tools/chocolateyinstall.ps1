$version = '3.8.9'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '16C46D6AB9375A2A1FFE7F3A11215DBD72317171B2154D5B286A93438B110990'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
