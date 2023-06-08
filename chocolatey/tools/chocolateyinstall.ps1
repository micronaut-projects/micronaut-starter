$version = '4.0.0-M3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '0FC5292A9D545533885C6990B8385D03BF06B38F28984EA74B4E5DB275C259A4'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
