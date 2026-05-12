$version = '5.0.0-RC1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'E9FE614E2B0BD9A7953F7C29CED4D05C72914A1074DE53C3BE80B9DF7C2F7F27'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
